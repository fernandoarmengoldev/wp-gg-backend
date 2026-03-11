import type { Db } from 'mongodb'

import { printStatus } from '../banner'
import { intervalFunc } from '../scripts/data-generator'

// Start the background job that keeps collecting random matches over time.
export function startRandomMatchesJob(db: Db, apiKey: string, intervalMs: number): void {
  // Inform startup logs whether the collector will run in the background.
  printStatus('JOB', 'Random match collection enabled', 'green')

  // Run the collector on a fixed interval so the dataset grows gradually.
  setInterval(() => {
    intervalFunc({ db, apiKey }).catch(error => {
      const message = error instanceof Error ? error.message : String(error)
      printStatus('ERROR', `Random match collection failed: ${message}`, 'red')
    })
  }, intervalMs)
}

// Log when the feature is turned off so startup behavior is explicit.
export function logRandomMatchesDisabled(): void {
  printStatus('JOB', 'Random match collection disabled', 'yellow')
}
