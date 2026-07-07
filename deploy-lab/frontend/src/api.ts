import axios, { AxiosError, type AxiosRequestConfig } from 'axios'
import type { ApiResponse, HttpInspectResult, Message, RequestRecord, SystemInfo } from './types'

const http = axios.create({
  baseURL: '/',
  timeout: 8000
})

export interface ApiError {
  message: string
  status?: number
  traceId?: string
}

export function toApiError(error: unknown): ApiError {
  if (axios.isAxiosError(error)) {
    const axiosError = error as AxiosError<ApiResponse<unknown>>
    return {
      message: axiosError.response?.data?.message ?? axiosError.message,
      status: axiosError.response?.status,
      traceId: axiosError.response?.data?.traceId
    }
  }
  return { message: error instanceof Error ? error.message : '请求失败' }
}

async function unwrap<T>(request: Promise<{ data: ApiResponse<T> }>): Promise<ApiResponse<T>> {
  return (await request).data
}

export const api = {
  systemInfo: () => unwrap<SystemInfo>(http.get('/api/system/info')),
  health: () => http.get('/actuator/health'),
  inspect: (method: string, url: string, body: string, config: AxiosRequestConfig) =>
    unwrap<HttpInspectResult>(http.request({ method, url, data: body || undefined, ...config })),
  listMessages: () => unwrap<Message[]>(http.get('/api/messages?limit=200')),
  createMessage: (payload: { title: string; content: string; expiresAt?: string | null }) =>
    unwrap<Message>(http.post('/api/messages', payload)),
  deleteMessage: (id: number) => unwrap<void>(http.delete(`/api/messages/${id}`)),
  clearMessages: () => unwrap<void>(http.delete('/api/messages')),
  listRequestRecords: () => unwrap<RequestRecord[]>(http.get('/api/request-records?limit=100')),
  clearRequestRecords: () => unwrap<void>(http.delete('/api/request-records')),
  delay: (milliseconds: number) => unwrap(http.get(`/api/diagnostics/delay?milliseconds=${milliseconds}`)),
  status: (code: number) => unwrap(http.get(`/api/diagnostics/status?code=${code}`, { validateStatus: () => true })),
  error: () => unwrap(http.get('/api/diagnostics/error'))
}
