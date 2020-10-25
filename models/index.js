const Sequelize = require("sequelize");

// Connect to CockroachDB through Sequelize.

let sequelize;
if(process.env.NODE_ENV === 'production'){
    
 sequelize = new Sequelize(
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
}else{
    sequelize = new Sequelize("dandy", "root", "uchiha", {
        dialect: "mysql",
        host: "127.0.0.1",
      });
}
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
