export interface ApiResponse<T> {
  code: number
  message: string
  data: T
  traceId: string
  timestamp: string
}

export interface SystemInfo {
  applicationName: string
  appVersion: string
  instanceId: string
  activeProfile: string
  startedAt: string
  uptimeSeconds: number
  javaVersion: string
  osName: string
  hostname: string
  containerName: string
  serverPort: number
  currentTime: string
  availableProcessors: number
  usedMemoryBytes: number
  maxMemoryBytes: number
  diagnosticsEnabled: boolean
}

export interface HttpInspectResult {
  method: string
  uri: string
  queryParameters: Record<string, string[]>
  headers: Record<string, string[]>
  contentType: string
  body: string
  bodyTruncated: boolean
  clientIp: string
  xRealIp: string
  xForwardedFor: string
  xForwardedProto: string
  host: string
  userAgent: string
  instanceId: string
  receivedAt: string
}

export interface Message {
  id: number
  title: string
  content: string
  createdAt: string
  expiresAt: string | null
}

export interface RequestRecord {
  id: number
  traceId: string
  method: string
  path: string
  status: number
  durationMs: number
  clientIp: string
  instanceId: string
  createdAt: string
}
