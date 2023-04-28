# pull official base image
FROM node:16.4.2-alpine as builder

# set working directory
WORKDIR /opt/e-wallet-fe

# add `/opt/e-wallet-fe/node_modules/.bin` to $PATH
ENV PATH /opt/e-wallet-fe/node_modules/.bin:$PATH

RUN addgroup --system e-wallet && adduser --system e-wallet-fe --ingroup e-wallet && \
	chown -R e-wallet-fe /opt/e-wallet-fe

# install app dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm install --silent
RUN npm install react-scripts@5.0.1 -g --silent

# add e-wallet-fe
COPY . ./

# build e-wallet-fe
RUN npm run build

FROM nginx:1.24.0-alpine-slim
COPY --from=builder /opt/e-wallet-fe/build /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx/nginx.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
