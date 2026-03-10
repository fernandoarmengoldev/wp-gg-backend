// MongoDB database type used to access the matches collection.
import type { Db } from 'mongodb'

// Shared match shape stored in the database and returned by Riot.
import type { MatchDocument } from '../types'

// Return a random element from an array, or undefined when the array is empty.
function randomItem<T>(items: T[]): T | undefined {
  return items[Math.floor(Math.random() * items.length)]
}

// Perform a Riot API request and parse the JSON body when the response is valid.
async function riotRequest<T>(url: string): Promise<T | null> {
  // Send the HTTP request to Riot.
  const response = await fetch(url)

  // Stop early when Riot responds with an error status.
  if (!response.ok) {
    return null
  }

  // Read the raw response body before parsing it as JSON.
  const text = await response.text()

  // Ignore empty responses because there is nothing useful to parse.
  if (!text) {
    return null
  }

  // Convert the JSON payload into the expected TypeScript shape.
  return JSON.parse(text) as T
}

// Periodically discover and store a new random match based on existing data.
export async function intervalFunc({ db, apiKey }: { db: Db; apiKey: string }): Promise<void> {
  // Use the matches collection as both the source and destination of data.
  const collection = db.collection<MatchDocument>('matches')

  // Pick one stored match at random to use as the starting point.
  const sampledMatch = randomItem(await collection.aggregate<MatchDocument>([{ $sample: { size: 1 } }]).toArray())

  // From that match, pick a random participant and use their PUUID to explore more matches.
  const puuid = sampledMatch && randomItem(sampledMatch.metadata.participants)

  // Stop if there is no stored match or no participant to follow.
  if (!puuid) {
    return
  }

  // Request a small slice of this player's recent match history.
  const history = await riotRequest<string[]>(`https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/${puuid}/ids?start=0&count=20&api_key=${apiKey}`)

  // Choose one of those match ids at random so the dataset grows organically.
  const matchId = Array.isArray(history) ? randomItem(history) : undefined

  // Stop if Riot did not return any usable match ids.
  if (!matchId) {
    return
  }

  // Fetch the full match payload and store it only when Riot returns valid match data.
  const match = await riotRequest<MatchDocument & { status?: unknown }>(`https://europe.api.riotgames.com/lol/match/v5/matches/${matchId}?api_key=${apiKey}`)

  // Ignore Riot error payloads and insert only complete match documents.
  if (match && !('status' in match)) {
    await collection.insertOne(match)
  }
}
