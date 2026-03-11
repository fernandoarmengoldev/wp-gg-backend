import 'dotenv/config'

import { getAppConfig } from './config'
import { connectToDatabase } from './db'
import { logRandomMatchesDisabled, startRandomMatchesJob } from './jobs/random-matches'
import { createRoutes } from './routes'

// Create a JSON response with a custom HTTP status code.
function createJsonResponse<T>(body: T, status = 200): Response {
  return Response.json(body, { status })
}

// Boot the database connection, optional background jobs, and the Bun HTTP server.
async function startServer(): Promise<void> {
  // Load the validated runtime configuration for the application.
  const { apiKey, getRandomMatches, port, randomMatchIntervalMs } = getAppConfig()

  // Connect to MongoDB before exposing the API routes.
  const db = await connectToDatabase()

  // Start or skip the background match collector based on the environment flag.
  if (getRandomMatches) {
    startRandomMatchesJob(db, apiKey, randomMatchIntervalMs)
  } else {
    logRandomMatchesDisabled()
  }

  // Start the HTTP server with the assembled route table and shared handlers.
  const server = Bun.serve({
    port,
    routes: createRoutes({ apiKey, db }),
    fetch() {
      return createJsonResponse({ message: 'Not Found' }, 404)
    },
    error(error) {
      console.error(error)
      return createJsonResponse({ message: 'Internal Server Error' }, 500)
    },
  })

  // Log the final listening port after Bun finishes booting the server.
  console.log(`Server running on port ${server.port}`)
}

// Exit with a failure code when startup cannot complete successfully.
startServer().catch(error => {
  console.error('Server startup failed', error)
  process.exit(1)
})
