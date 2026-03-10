import type { Document } from 'mongodb'

export type BunRouteRequest<TParams extends Record<string, string>> = Request & {
  params: TParams
}

export type NumberMap = Record<string, number>

export interface RiotErrorResponse {
  status?: unknown
  history?: string[]
  [key: string]: unknown
}

export interface SummonerResponse {
  puuid: string
  history?: string[]
  [key: string]: unknown
}

export interface MatchPerkStyleSelection {
  perk: number
}

export interface MatchPerkStyle {
  style: number
  selections: MatchPerkStyleSelection[]
}

export interface MatchParticipant {
  championId: number
  win: boolean
  item0: number
  item1: number
  item2: number
  item3: number
  item4: number
  item5: number
  summoner1Id: number
  summoner2Id: number
  perks: {
    statPerks: Record<string, number>
    styles: MatchPerkStyle[]
  }
}

export interface MatchDocument extends Document {
  metadata: {
    participants: string[]
  }
  info: {
    gameMode: string
    participants: MatchParticipant[]
  }
}
