var fs = require('fs');
var Sequelize = require('sequelize-cockroachdb');

// Connect to CockroachDB through Sequelize.
var sequelize = new Sequelize('parking', 'testuser', 'testpassword', {
    host:"gifted-ox-5wk.gcp-northamerica-northeast1.cockroachlabs.cloud",
    dialect: 'postgres',
    port: 26257,
    logging: false,
    dialectOptions: {
        ssl: {
            ca: fs.readFileSync('certs/gifted-ox-ca.crt')
                .toString()
        }
    }
});

const models = {
    account: sequelize.import("./account"),
    parkingspot: sequelize.import("./parkingspot")

  };
  
  Object.keys(models).forEach((modelName) => {
    if ("associate" in models[modelName]) {
      models[modelName].associate(models);
    }
  });
  
  models.sequelize = sequelize;
  models.Sequelize = Sequelize;
  module.exports = models;
  
