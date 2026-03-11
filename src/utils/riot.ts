// Perform a Riot API request and parse the JSON body when present.
export async function riotRequest<T>(url: string): Promise<T | null> {
  // Send the HTTP request to the Riot API.
  const response = await fetch(url)

  // Stop early when Riot responds with a non-success status.
  if (!response.ok) {
    return null
  }

  // Read the raw body first so empty responses can be handled safely.
  const text = await response.text()

  // Return null when Riot sends an empty body.
  if (!text) {
    return null
  }

  // Convert the JSON payload into the expected TypeScript shape.
  return JSON.parse(text) as T
}
