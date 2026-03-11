import { MongoClient, type Db } from 'mongodb'

// Fixed database name used by the application.
const DB_NAME = 'lol'

// Create a MongoDB connection and return the application database instance.
export async function connectToDatabase(): Promise<Db> {
  // Read the connection string from the environment.
  const url = process.env.DB_URL

  // Stop early when the database URL is missing.
  if (!url) {
    throw new Error('DB_URL no esta definido')
  }

  // Create the MongoDB client with the configured connection string.
  const client = new MongoClient(url)

  try {
    // Open the database connection before returning the selected database.
    await client.connect()
    console.log('Connected to the database')
    return client.db(DB_NAME)
  } catch (error) {
    // Log the connection failure and rethrow so the server startup stops.
    console.error('Failed to connect to the database')
    console.error(error)
    throw error
  }
}
