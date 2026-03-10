import 'dotenv/config'

import { MongoClient, type Db } from 'mongodb'

import { getChampionStatsHandler, getPositionsHandler, rebuildPositionsHandler } from './routes/champion'
import { getEloHandler, getMasteryHandler, getMatchHandler, getSummonerHandler } from './routes/summoner'
import { intervalFunc } from './scripts/data-generator'
import type { BunRouteRequest } from './types'

function createJsonResponse<T>(body: T, status = 200): Response {
  return Response.json(body, { status })
}

async function startServer(): Promise<void> {
  const apiKey = process.env.RIOT_API_KEY
  const getRandomMatches = process.env.GET_RANDOM_MATCHES === 'true'
  const port = Number(process.env.PORT || 3000)
  const url = process.env.DB_URL

  if (!apiKey) {
    throw new Error('RIOT_API_KEY no esta definido')
  }

  if (!url) {
    throw new Error('DB_URL no esta definido')
  }

  const client = new MongoClient(url)

  try {
    await client.connect()
    console.log('Conectado a la base de datos')
  } catch (error) {
    console.log('No se puede conectar a la base de datos')
    console.log(error)
    throw error
  }

  const db = client.db('lol')

  if (getRandomMatches) {
    console.log('Random match collection is enabled')
    startRandomMatchesJob(db, apiKey)
  } else {
    console.log('Random match collection is disabled')
  }

  const server = Bun.serve({
    port,
    routes: {
      '/': () => createJsonResponse({ message: 'Servidor Activo' }),
      '/api/summoner/:name': req => getSummonerHandler(req as BunRouteRequest<{ name: string }>, { apiKey }),
      '/api/maestry/:id': req => getMasteryHandler(req as BunRouteRequest<{ id: string }>, { apiKey }),
      '/api/elo/:id': req => getEloHandler(req as BunRouteRequest<{ id: string }>, { apiKey }),
      '/api/match/:matchid': req => getMatchHandler(req as BunRouteRequest<{ matchid: string }>, { apiKey }),
      '/api/matchesFromChamp/:champ': req => getChampionStatsHandler(req as BunRouteRequest<{ champ: string }>, { db }),
      '/api/position': () => getPositionsHandler({ db }),
      '/api/positionSetting': () => rebuildPositionsHandler({ db }),
    },
    fetch() {
      return createJsonResponse({ message: 'Not Found' }, 404)
    },
    error(error) {
      console.error(error)
      return createJsonResponse({ message: 'Internal Server Error' }, 500)
    },
  })

  console.log('Server on port', server.port)
}

function startRandomMatchesJob(db: Db, apiKey: string): void {
  setInterval(() => {
    intervalFunc({ db, apiKey }).catch(error => {
      console.error('Error en la recoleccion automatica', error)
    })
  }, 5000)
}

startServer().catch(() => {
  process.exit(1)
})
