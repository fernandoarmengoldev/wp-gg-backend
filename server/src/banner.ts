// ANSI escape codes used to color the startup banner and status lines.
const ANSI = {
  blue: '\u001b[34m',
  bright: '\u001b[1m',
  cyan: '\u001b[36m',
  dim: '\u001b[2m',
  green: '\u001b[32m',
  red: '\u001b[31m',
  reset: '\u001b[0m',
  yellow: '\u001b[33m',
} as const

// ASCII art shown when the server boots in an interactive terminal.
const STARTUP_BANNER = String.raw`
 ___       __   ________   ________  ________     
|\  \     |\  \|\   __  \ |\   ____\|\   ____\    
\ \  \    \ \  \ \  \|\  \\ \  \___|\ \  \___|    
 \ \  \  __\ \  \ \   ____\\ \  \  __\ \  \  ___  
  \ \  \|\__\_\  \ \  \___|_\ \  \|\  \ \  \|\  \ 
   \ \____________\ \__\ |\__\ \_______\ \_______\
    \|____________|\|__| \|__|\|_______|\|_______|
`.trim().split('\n')

type BannerColor = 'blue' | 'cyan' | 'dim' | 'green' | 'red' | 'yellow'

// Wait briefly between lines to create a lightweight reveal animation.
function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms))
}

// Wrap text with one or more ANSI styles and reset formatting afterward.
function colorize(text: string, ...styles: string[]): string {
  return `${styles.join('')}${text}${ANSI.reset}`
}

// Paint the logo in red while preserving a plain-text fallback.
function getBannerArtLines(useColor: boolean): string[] {
  return STARTUP_BANNER.map(line => (useColor ? colorize(line, ANSI.bright, ANSI.red) : line))
}

// Format a labeled log line such as BOOT, DB, or READY with an optional color.
function formatStatusLine(label: string, message: string, useColor: boolean, color: BannerColor): string {
  const line = `[${label}] ${message}`

  return useColor ? colorize(line, color === 'dim' ? ANSI.dim : ANSI.bright, ANSI[color]) : line
}

// Build the final ready-state lines displayed once the server is listening.
function getReadyLines(port: number, useColor: boolean): string[] {
  const divider = '-'.repeat(44)
  const readyLine = `[READY] Arena online at http://localhost:${port}`
  const hintLine = '[HINT] Press Ctrl+C to stop'

  return [
    useColor ? colorize(divider, ANSI.dim, ANSI.red) : divider,
    useColor ? colorize(readyLine, ANSI.bright, ANSI.green) : readyLine,
    useColor ? colorize(hintLine, ANSI.dim, ANSI.cyan) : hintLine,
    '',
  ]
}

// Detect whether the current output supports animation and ANSI colors.
function getTerminalMode(): { isInteractiveTerminal: boolean; useColor: boolean } {
  const isInteractiveTerminal = Boolean(process.stdout.isTTY)
  const useColor = isInteractiveTerminal && !process.env.NO_COLOR

  return { isInteractiveTerminal, useColor }
}

// Print lines immediately in non-interactive environments or animate them in a TTY.
async function writeLines(lines: string[], isInteractiveTerminal: boolean): Promise<void> {
  if (!isInteractiveTerminal) {
    for (const line of lines) {
      console.log(line)
    }

    return
  }

  for (const line of lines) {
    process.stdout.write(`${line}\n`)
    await delay(45)
  }
}

// Render the animated startup banner shown at the beginning of boot.
export async function printStartupBanner(): Promise<void> {
  const { isInteractiveTerminal, useColor } = getTerminalMode()
  const lines = [
    '',
    ...getBannerArtLines(useColor),
    '',
    formatStatusLine('BOOT', 'Summoning server...', useColor, 'yellow'),
    '',
  ]

  await writeLines(lines, isInteractiveTerminal)
}

// Print a single color-coded status line during startup.
export function printStatus(label: string, message: string, color: BannerColor): void {
  const { useColor } = getTerminalMode()

  console.log(formatStatusLine(label, message, useColor, color))
}

// Print the final ready-state block after Bun starts listening.
export function printReadyBanner(port: number): void {
  const { useColor } = getTerminalMode()

  for (const line of getReadyLines(port, useColor)) {
    console.log(line)
  }
}
