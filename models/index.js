var Sequelize = require('sequelize');

// Connect to CockroachDB through Sequelize.
var sequelize = new Sequelize('parking', 'dandyuser', 'test', {
    host:"dandy-293601:northamerica-northeast1:dandydb",
    dialect: 'mysql',
    port: 3306,
    logging: false,
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
  
