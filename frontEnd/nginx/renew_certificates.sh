#!/bin/sh

# Renew the certificates
certbot renew

# Reload Nginx to apply the new certificates
nginx -s reload