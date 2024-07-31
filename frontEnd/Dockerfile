# Use the official Node.js image as the base image
FROM node:16-alpine

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Vue.js application
RUN npm run build

# Use the official Nginx image as the base image
FROM nginx:stable-alpine

# Copy the build output to the Nginx HTML directory
COPY --from=0 ./dist /usr/share/nginx/html

# Copy the Nginx configuration file
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
