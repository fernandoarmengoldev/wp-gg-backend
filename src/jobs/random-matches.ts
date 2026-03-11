import type { Db } from 'mongodb'

import { intervalFunc } from '../scripts/data-generator'

// Start the background job that keeps collecting random matches over time.
export function startRandomMatchesJob(db: Db, apiKey: string, intervalMs: number): void {
  // Inform startup logs whether the collector will run in the background.
  console.log('Random match collection is enabled')

  // Run the collector on a fixed interval so the dataset grows gradually.
  setInterval(() => {
    intervalFunc({ db, apiKey }).catch(error => {
      console.error('Random match collection failed', error)
    })
  }, intervalMs)
}

// Log when the feature is turned off so startup behavior is explicit.
export function logRandomMatchesDisabled(): void {
  console.log('Random match collection is disabled')
}
