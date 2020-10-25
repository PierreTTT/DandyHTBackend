const Sequelize = require("sequelize");

// Connect to CockroachDB through Sequelize.
const sequelize = new Sequelize(
  process.env.DATABASE_NAME,
  process.env.USERNAME,
  process.env.PASSWORD,
  {
    dialect: process.env.DIALECT,
    host: `/cloudsql/${process.env.CONNECTION_NAME}`,
    dialectOptions: {
      socketPath: `/cloudsql/${process.env.CONNECTION_NAME}`,
    },
  }
);
const models = {
  account: sequelize.import("./account"),
  parkingspot: sequelize.import("./parkingspot"),
};

Object.keys(models).forEach((modelName) => {
  if ("associate" in models[modelName]) {
    models[modelName].associate(models);
  }
});

models.sequelize = sequelize;
models.Sequelize = Sequelize;
module.exports = models;
