import type { Db } from 'mongodb'

import type { BunRouteRequest, MatchDocument, NumberMap } from '../types'
import { buildRuneKey, createPercentageEntries, getBottomEntries, getTopEntries, incrementMap } from './champion-utils'

type ChampionStatsResponse = {
  winRate?: string
  pickRate?: string
  bestFive: Array<{ id: string; value: string }>
  worstFive?: Array<{ id: string; value: string }>
  boots?: Array<{ value: string; porcentaje: string }>
  mythics?: Array<{ value: string; porcentaje: string }>
  otherItem?: Array<{ value: string; porcentaje: string }>
  spells?: Array<{ value: string; porcentaje: string }>
  runes?: Array<{ value: string; porcentaje: string }>
}

// Item id groups used to classify completed builds.
const BOOTS = new Set([3009, 3117, 3158, 3006, 3047, 3020, 3111])
const MYTHICS = new Set([4644, 6632, 6691, 6692, 3001, 6656, 6662, 6671, 6630, 3152, 6673, 4005, 6672, 6653, 3190, 6655, 6617, 4636, 6693, 4633, 2065, 6631, 3068, 3078, 6664])
const LEGENDARIES = new Set([3031, 3109, 3100, 3036, 3004, 3156, 3041, 3139, 3222, 8020, 8001, 3003, 3504, 6696, 3102, 3071, 3153, 3072, 6609, 3011, 4629, 3742, 6333, 4637, 3814, 3508, 3121, 4401, 3110, 3193, 3026, 3124, 4628, 3181, 3165, 3033, 3042, 3115, 6675, 3046, 3089, 3143, 3094, 3074, 3107, 3085, 3116, 3040, 6695, 6694, 4645, 6035, 3065, 6616, 3053, 6676, 3075, 3748, 3179, 3135, 3083, 3119, 3091, 3142, 3050, 3157])

// Aggregate stored matches to compute build, matchup, spell, and rune statistics for one champion.
export async function getChampionStatsHandler(
  req: BunRouteRequest<{ champ: string }>,
  { db }: { db: Db },
): Promise<Response> {
  // Parse the champion id from the route params.
  const championId = Number.parseInt(req.params.champ, 10)

  // Use the matches collection as the source for all derived analytics.
  const collection = db.collection<MatchDocument>('matches')

  // Only consider classic matches where the target champion appears.
  const matches = await collection.find({
    'info.participants.championId': championId,
    'info.gameMode': 'CLASSIC',
  }).toArray()

  // Maps used to count item, spell, rune, and matchup frequency.
  const hashMapBoots: NumberMap = {}
  const hashMapMythics: NumberMap = {}
  const hashMapOther: NumberMap = {}
  const hashMapSpells: NumberMap = {}
  const hashMapRunes: NumberMap = {}
  const goodAgainst: NumberMap = {}
  const totalEachChamp: NumberMap = {}

  // Count how many of the sampled matches were victories.
  let wins = 0

  // Inspect every participant across every selected match.
  matches.forEach(match => {
    match.info.participants.forEach((participant, index) => {
      // Ignore participants that are not playing the requested champion.
      if (participant.championId !== championId) {
        return
      }

      // Use the mirrored enemy slot to estimate the direct lane opponent.
      const enemyParticipant = match.info.participants[index < 5 ? index + 5 : index - 5]
      if (!enemyParticipant) {
        return
      }

      // Add positive or negative matchup score depending on the game result.
      if (participant.win) {
        wins += 1
        incrementMap(goodAgainst, enemyParticipant.championId)
      } else {
        incrementMap(goodAgainst, enemyParticipant.championId, -1)
      }

      // Track how many times this opponent champion appeared overall.
      incrementMap(totalEachChamp, enemyParticipant.championId)

        // Categorize each completed item slot into boots, mythics, or other items.
        ;[participant.item0, participant.item1, participant.item2, participant.item3, participant.item4, participant.item5].forEach(item => {
          if (BOOTS.has(item)) {
            incrementMap(hashMapBoots, item)
          } else if (MYTHICS.has(item)) {
            incrementMap(hashMapMythics, item)
          } else if (LEGENDARIES.has(item)) {
            incrementMap(hashMapOther, item)
          }
        })

        // Count both summoner spells used by this participant.
        ;[participant.summoner1Id, participant.summoner2Id].forEach(spell => {
          incrementMap(hashMapSpells, spell)
        })

      // Count the full rune page as a single configuration.
      incrementMap(hashMapRunes, buildRuneKey(participant))
    })
  })

  // Start building the response object with the required fields.
  const response: ChampionStatsResponse = { bestFive: [] }
  if (matches.length === 0) {
    return Response.json(response)
  }

  // Compute overall win rate and pick rate for this champion.
  response.winRate = ((wins / matches.length) * 100).toFixed(2)
  response.pickRate = ((matches.length / (await collection.countDocuments())) * 100).toFixed(2)

  // Remove extreme matchup values that likely represent unreliable sample sizes.
  Object.keys(goodAgainst).forEach(key => {
    const total = totalEachChamp[key]
    const against = goodAgainst[key]
    if (total === undefined || against === undefined) {
      return
    }

    const ratio = ((total + against) / (total * 2)) * 100
    if (ratio >= 70 || ratio <= 30) {
      delete goodAgainst[key]
    }
  })

  // Extract the strongest and weakest matchup groups.
  const bestFive = getTopEntries(4, goodAgainst)
  const worstFive = getBottomEntries(4, goodAgainst)
  const createMatchupValue = (name: string, value: number): string => (((totalEachChamp[name] ?? 0) + value) / ((totalEachChamp[name] ?? 1) * 2) * 100).toFixed(2)

  // Convert matchup scores into normalized percentages.
  response.bestFive = bestFive.map(entry => ({ id: entry.name, value: createMatchupValue(entry.name, entry.value) }))
  response.worstFive = worstFive.map(entry => ({ id: entry.name, value: createMatchupValue(entry.name, entry.value) }))

  // Attach the most common builds, spells, and rune pages.
  response.boots = createPercentageEntries(getTopEntries(2, hashMapBoots), matches.length)
  response.mythics = createPercentageEntries(getTopEntries(2, hashMapMythics), matches.length)
  response.otherItem = createPercentageEntries(getTopEntries(3, hashMapOther), matches.length)
  response.spells = createPercentageEntries(getTopEntries(3, hashMapSpells), matches.length)
  response.runes = createPercentageEntries(getTopEntries(2, hashMapRunes), matches.length)

  return Response.json(response)
}
