FROM mongo:4.4.21
ADD mongodb/scripts/init_replicaset.js init_replicaset.js
