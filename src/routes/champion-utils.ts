import type { MatchParticipant, NumberMap } from '../types'

export type RankedEntry = {
  name: string
  value: number
}

// Increase or decrease a numeric counter stored inside a string-keyed map.
export function incrementMap(map: NumberMap, key: number | string, amount = 1): void {
  const normalizedKey = String(key)
  map[normalizedKey] = (map[normalizedKey] ?? 0) + amount
}

// Convert ranked entries into percentage strings based on the provided total.
export function createPercentageEntries(entries: RankedEntry[], total: number): Array<{ value: string; porcentaje: string }> {
  return entries.map(entry => ({
    value: entry.name,
    porcentaje: ((entry.value / total) * 100).toFixed(2),
  }))
}

// Serialize all rune selections for a participant into a comparable string key.
export function buildRuneKey(participant: MatchParticipant): string {
  const runeParts: string[] = []

  // Add the stat perk values first.
  Object.entries(participant.perks.statPerks).forEach(([statName, value]) => {
    runeParts.push(`${statName},${value}`)
  })

  // Then add the style ids and each selected rune id.
  participant.perks.styles.forEach(style => {
    runeParts.push(String(style.style))
    style.selections.forEach(selection => {
      runeParts.push(String(selection.perk))
    })
  })

  // Join every rune fragment into a single stable map key.
  return runeParts.join(',')
}

// Return the highest-value entries from a numeric map.
export function getTopEntries(numElem: number, hashMap: NumberMap): RankedEntry[] {
  return Object.entries(hashMap)
    .map(([name, value]) => ({ name, value }))
    .sort((a, b) => b.value - a.value)
    .slice(0, numElem)
}

// Return the lowest-value entries from a numeric map.
export function getBottomEntries(numElem: number, hashMap: NumberMap): RankedEntry[] {
  return Object.entries(hashMap)
    .map(([name, value]) => ({ name, value }))
    .sort((a, b) => a.value - b.value)
    .slice(0, numElem)
}
