import type { Db } from 'mongodb'

import type { BunRouteRequest, MatchDocument, MatchParticipant, NumberMap } from '../types'

type RankedEntry = {
  name: string
  value: number
}

type PositionKey = 'top' | 'jg' | 'mid' | 'bot' | 'supp'

type PositionCounts = Record<PositionKey, number>

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

function jsonResponse<T>(body: T, status = 200): Response {
  return Response.json(body, { status })
}

function incrementMap(map: NumberMap, key: number | string, amount = 1): void {
  const normalizedKey = String(key)
  map[normalizedKey] = (map[normalizedKey] ?? 0) + amount
}

function createPercentageEntries(entries: RankedEntry[], total: number): Array<{ value: string; porcentaje: string }> {
  return entries.map(entry => ({
    value: entry.name,
    porcentaje: ((entry.value / total) * 100).toFixed(2),
  }))
}

function buildRuneKey(participant: MatchParticipant): string {
  const runeParts: string[] = []

  Object.entries(participant.perks.statPerks).forEach(([statName, value]) => {
    runeParts.push(`${statName},${value}`)
  })

  participant.perks.styles.forEach(style => {
    runeParts.push(String(style.style))
    style.selections.forEach(selection => {
      runeParts.push(String(selection.perk))
    })
  })

  return runeParts.join(',')
}

export async function getChampionStatsHandler(
  req: BunRouteRequest<{ champ: string }>,
  { db }: { db: Db },
): Promise<Response> {
  const championId = Number.parseInt(req.params.champ, 10)
  const collection = db.collection<MatchDocument>('matches')

  const cursor = collection.find({
    'info.participants.championId': championId,
    'info.gameMode': 'CLASSIC',
  })
  const matches = await cursor.toArray()

  const boots = [3009, 3117, 3158, 3006, 3047, 3020, 3111]
  const mythics = [4644, 6632, 6691, 6692, 3001, 6656, 6662, 6671, 6630, 3152, 6673, 4005, 6672, 6653, 3190, 6655, 6617, 4636, 6693, 4633, 2065, 6631, 3068, 3078, 6664]
  const legendaries = [3031, 3109, 3100, 3036, 3004, 3156, 3041, 3139, 3222, 8020, 8001, 3003, 3504, 6696, 3102, 3071, 3153, 3072, 6609, 3011, 4629, 3742, 6333, 4637, 3814, 3508, 3121, 4401, 3110, 3193, 3026, 3124, 4628, 3181, 3165, 3033, 3042, 3115, 6675, 3046, 3089, 3143, 3094, 3074, 3107, 3085, 3116, 3040, 6695, 6694, 4645, 6035, 3065, 6616, 3053, 6676, 3075, 3748, 3179, 3135, 3083, 3119, 3091, 3142, 3050, 3157]

  const hashMapBoots: NumberMap = {}
  const hashMapMythics: NumberMap = {}
  const hashMapOther: NumberMap = {}
  const hashMapSpells: NumberMap = {}
  const hashMapRunes: NumberMap = {}
  const goodAgainst: NumberMap = {}
  const totalEachChamp: NumberMap = {}

  let win = 0

  matches.forEach(match => {
    match.info.participants.forEach((participant, index) => {
      if (participant.championId !== championId) {
        return
      }

      const enemyIndex = index < 5 ? index + 5 : index - 5
      const enemyParticipant = match.info.participants[enemyIndex]

      if (!enemyParticipant) {
        return
      }

      try {
        if (participant.win) {
          win++
          incrementMap(goodAgainst, enemyParticipant.championId)
        } else {
          incrementMap(goodAgainst, enemyParticipant.championId, -1)
        }

        incrementMap(totalEachChamp, enemyParticipant.championId)
      } catch (error) {
        console.error(error, enemyIndex)
      }

      const items = [participant.item0, participant.item1, participant.item2, participant.item3, participant.item4, participant.item5]
      items.forEach(item => {
        if (boots.includes(item)) {
          incrementMap(hashMapBoots, item)
        } else if (mythics.includes(item)) {
          incrementMap(hashMapMythics, item)
        } else if (legendaries.includes(item)) {
          incrementMap(hashMapOther, item)
        }
      })

      const spells = [participant.summoner1Id, participant.summoner2Id]
      spells.forEach(spell => {
        incrementMap(hashMapSpells, spell)
      })

      incrementMap(hashMapRunes, buildRuneKey(participant))
    })
  })

  const json: ChampionStatsResponse = {
    bestFive: [],
  }

  if (matches.length >= 1) {
    json.winRate = ((win / matches.length) * 100).toFixed(2)
    json.pickRate = ((matches.length / (await collection.countDocuments())) * 100).toFixed(2)

    for (const key in goodAgainst) {
      const total = totalEachChamp[key]
      const against = goodAgainst[key]

      if (total === undefined || against === undefined) {
        continue
      }

      const num = ((total + against) / (total * 2)) * 100
      if (num >= 70 || num <= 30) {
        delete goodAgainst[key]
      }
    }

    const bestFive = getFirstNElements(4, goodAgainst)
    const worstFive = getLastNElements(4, goodAgainst)

    json.bestFive = bestFive.map(entry => ({
      id: entry.name,
      value: (((totalEachChamp[entry.name] ?? 0) + entry.value) / ((totalEachChamp[entry.name] ?? 1) * 2) * 100).toFixed(2),
    }))

    json.worstFive = worstFive.map(entry => ({
      id: entry.name,
      value: (((totalEachChamp[entry.name] ?? 0) + entry.value) / ((totalEachChamp[entry.name] ?? 1) * 2) * 100).toFixed(2),
    }))

    json.boots = createPercentageEntries(getFirstNElements(2, hashMapBoots), matches.length)
    json.mythics = createPercentageEntries(getFirstNElements(2, hashMapMythics), matches.length)
    json.otherItem = createPercentageEntries(getFirstNElements(3, hashMapOther), matches.length)
    json.spells = createPercentageEntries(getFirstNElements(3, hashMapSpells), matches.length)
    json.runes = createPercentageEntries(getFirstNElements(2, hashMapRunes), matches.length)
  }

  console.log(json)
  return jsonResponse(json)
}

export async function getPositionsHandler({ db }: { db: Db }): Promise<Response> {
  const collection = db.collection('positions')
  const cursor = collection.find().project({ _id: 0, value: 1, positions: 1 })
  const matches = await cursor.toArray()

  console.log(matches)
  return jsonResponse(matches)
}

export async function rebuildPositionsHandler({ db }: { db: Db }): Promise<Response> {
  const collectionPositions = db.collection('positions')
  await collectionPositions.deleteMany({})

  const collectionMatches = db.collection<MatchDocument>('matches')
  const cursor = collectionMatches.find({ 'info.gameMode': 'CLASSIC' })
  const matches = await cursor.toArray()

  const champions = new Set<number>()

  matches.forEach(match => {
    match.info.participants.forEach(participant => {
      champions.add(participant.championId)
    })
  })

  for (const champ of champions) {
    if (champ > 1000) {
      continue
    }

    const positions: PositionCounts = {
      top: 0,
      jg: 0,
      mid: 0,
      bot: 0,
      supp: 0,
    }

    matches.forEach(match => {
      match.info.participants.forEach((participant, index) => {
        if (participant.championId !== champ) {
          return
        }

        switch (index) {
          case 0:
          case 5:
            positions.top += 1
            break
          case 1:
          case 6:
            positions.jg += 1
            break
          case 2:
          case 7:
            positions.mid += 1
            break
          case 3:
          case 8:
            positions.bot += 1
            break
          case 4:
          case 9:
            positions.supp += 1
            break
          default:
            break
        }
      })
    })

    const total = positions.top + positions.jg + positions.mid + positions.bot + positions.supp
    const porcentajes = getFirstNElements(5, positions)
    const posicionesFinales: string[] = []
    let tempPorcentaje = 0

    porcentajes.forEach(porcentaje => {
      if (tempPorcentaje < 65 && total > 0) {
        tempPorcentaje += (porcentaje.value / total) * 100
        posicionesFinales.push(porcentaje.name)
      }
    })

    await collectionPositions.insertOne({ value: champ, positions: posicionesFinales })
  }

  return jsonResponse('COMPLETADO')
}

function getFirstNElements(numElem: number, hashMap: NumberMap): RankedEntry[] {
  const array: RankedEntry[] = []

  for (const key in hashMap) {
    array.push({
      name: key,
      value: hashMap[key] ?? 0,
    })
  }

  return array
    .sort((a, b) => (a.value > b.value ? -1 : b.value > a.value ? 1 : 0))
    .slice(0, numElem)
}

function getLastNElements(numElem: number, hashMap: NumberMap): RankedEntry[] {
  const array: RankedEntry[] = []

  for (const key in hashMap) {
    array.push({
      name: key,
      value: hashMap[key] ?? 0,
    })
  }

  return array
    .sort((a, b) => (a.value > b.value ? 1 : b.value > a.value ? -1 : 0))
    .slice(0, numElem)
}
