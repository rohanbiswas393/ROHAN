FROM mongo:4.4.20
ADD mongodb/scripts/init_replicaset.js init_replicaset.js
