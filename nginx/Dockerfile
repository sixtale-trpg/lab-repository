# Use the official Nginx image as the base image
FROM nginx:stable-alpine

# Install Certbot
RUN apk add --no-cache certbot certbot-nginx

# Copy the Nginx configuration file
COPY /frontEnd/nginx.conf /etc/nginx/sites-available/default

# Add a script to renew certificates
COPY renew_certificates.sh /etc/periodic/weekly/renew_certificates

# Make the script executable
RUN chmod +x /etc/periodic/weekly/renew_certificates

# Expose ports 80 and 443
EXPOSE 80 443

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]