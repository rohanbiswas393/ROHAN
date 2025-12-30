FROM mongo:4.4.30
ADD mongodb/scripts/init_replicaset.js init_replicaset.js
