FROM mongo:4.4.23
ADD mongodb/scripts/init_replicaset.js init_replicaset.js
