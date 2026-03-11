import type { Db } from 'mongodb'

import type { MatchDocument } from '../types'
import { getTopEntries, incrementMap } from './champion-utils'

type PositionKey = 'top' | 'jg' | 'mid' | 'bot' | 'supp'
type PositionCounts = Record<PositionKey, number>

const POSITION_KEYS: PositionKey[] = ['top', 'jg', 'mid', 'bot', 'supp']
const POSITION_BY_INDEX: Record<number, PositionKey> = {
  0: 'top',
  1: 'jg',
  2: 'mid',
  3: 'bot',
  4: 'supp',
  5: 'top',
  6: 'jg',
  7: 'mid',
  8: 'bot',
  9: 'supp',
}

// Return the cached champion position data stored in the database.
export async function getPositionsHandler({ db }: { db: Db }): Promise<Response> {
  // Read the position documents while omitting the MongoDB id field.
  const positions = await db.collection('positions').find().project({ _id: 0, value: 1, positions: 1 }).toArray()
  return Response.json(positions)
}

// Rebuild the cached champion position table by scanning all classic matches.
export async function rebuildPositionsHandler({ db }: { db: Db }): Promise<Response> {
  // Start from a clean position cache before recalculating values.
  const collectionPositions = db.collection('positions')
  await collectionPositions.deleteMany({})

  // Load all classic matches that can contribute to position inference.
  const matches = await db.collection<MatchDocument>('matches').find({ 'info.gameMode': 'CLASSIC' }).toArray()

  // Count champion appearances by role in a single pass through the dataset.
  const championPositions: Record<number, PositionCounts> = {}
  matches.forEach(match => {
    match.info.participants.forEach((participant, index) => {
      if (participant.championId > 1000) {
        return
      }

      const position = POSITION_BY_INDEX[index]
      if (!position) {
        return
      }

      championPositions[participant.championId] ??= { top: 0, jg: 0, mid: 0, bot: 0, supp: 0 }
      const positionCounts = championPositions[participant.championId]
      if (positionCounts) {
        incrementMap(positionCounts, position)
      }
    })
  })

  // Convert raw counts into the most representative set of roles for each champion.
  const documents = Object.entries(championPositions).map(([championId, positions]) => ({
    value: Number(championId),
    positions: getPrimaryPositions(positions),
  }))

  if (documents.length > 0) {
    await collectionPositions.insertMany(documents)
  }

  return Response.json('COMPLETED')
}

// Return the most representative positions until the accumulated share reaches roughly 65%.
function getPrimaryPositions(positions: PositionCounts): string[] {
  const total = POSITION_KEYS.reduce((sum, key) => sum + positions[key], 0)
  if (total === 0) {
    return []
  }

  const primaryPositions: string[] = []
  let accumulatedPercentage = 0

  getTopEntries(POSITION_KEYS.length, positions).forEach(entry => {
    if (accumulatedPercentage >= 65) {
      return
    }

    accumulatedPercentage += (entry.value / total) * 100
    primaryPositions.push(entry.name)
  })

  return primaryPositions
}
