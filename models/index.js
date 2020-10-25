const Sequelize = require("sequelize");
const dotenv = require("dotenv");
dotenv.config();
// Connect to CockroachDB through Sequelize.

let sequelize;
if(process.env.NODE_ENV === 'production'){
    
 sequelize = new Sequelize(
  process.env.DATABASE_NAME,
  process.env.USERNAME,
  process.env.PASSWORD,
  {
    dialect: process.env.DIALECT,
    host: process.env.HOST,
    port:3306
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
