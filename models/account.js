module.exports= (sequelize,Sequelize)=>{
    const account = sequelize.define("account", {
    id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    username: {
        type: Sequelize.STRING
    },
    password: {
        type: Sequelize.STRING
    },
    points:{
        type: Sequelize.INTEGER
    }
    });
  
    account.associate = function(models){
        account.belongsTo(models.parkingspot,{foreignKey:'parkingLotId'})
  }
  
  return account;
  }