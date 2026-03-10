import type { BunRouteRequest, RiotErrorResponse, SummonerResponse } from '../types'

function jsonResponse<T>(body: T, status = 200): Response {
  return Response.json(body, { status })
}

async function riotRequest<T>(url: string): Promise<T | null> {
  const response = await fetch(url)
  const text = await response.text()

  if (!text) {
    return null
  }

  return JSON.parse(text) as T
}

export async function getSummonerHandler(
  req: BunRouteRequest<{ name: string }>,
  { apiKey }: { apiKey: string },
): Promise<Response> {
  const { name } = req.params
  const summonerUrl = `https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/${name}?api_key=${apiKey}`
  const json = await riotRequest<SummonerResponse | RiotErrorResponse>(summonerUrl)

  if (!json || 'status' in json) {
    const payload: RiotErrorResponse = json ?? { history: ['404'] }
    if (!payload.history) {
      payload.history = ['404']
    }
    return jsonResponse(payload)
  }

  const historyUrl = `https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/${json.puuid}/ids?start=0&count=20&api_key=${apiKey}`
  const history = await riotRequest<string[]>(historyUrl)

  json.history = Array.isArray(history) ? history.map(match => match.toString()) : []
  console.log(json)

  return jsonResponse(json)
}

export async function getMasteryHandler(
  req: BunRouteRequest<{ id: string }>,
  { apiKey }: { apiKey: string },
): Promise<Response> {
  const { id } = req.params
  const url = `https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/${id}?api_key=${apiKey}`
  const data = await riotRequest<unknown>(url)
  return jsonResponse(data)
}

export async function getEloHandler(
  req: BunRouteRequest<{ id: string }>,
  { apiKey }: { apiKey: string },
): Promise<Response> {
  const { id } = req.params
  const url = `https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/${id}?api_key=${apiKey}`
  const data = await riotRequest<unknown>(url)
  return jsonResponse(data)
}

export async function getMatchHandler(
  req: BunRouteRequest<{ matchid: string }>,
  { apiKey }: { apiKey: string },
): Promise<Response> {
  const { matchid } = req.params
  const url = `https://europe.api.riotgames.com/lol/match/v5/matches/${matchid}?api_key=${apiKey}`
  const data = await riotRequest<unknown>(url)
  console.log(data)
  return jsonResponse(data)
}
