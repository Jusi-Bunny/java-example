FROM node:lts-alpine AS build

WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY deploy/docker/nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf
