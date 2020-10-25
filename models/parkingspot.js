module.exports= (sequelize,Sequelize)=>{
    const parkingspot = sequelize.define("parkingspot", {
        id: {
            type: Sequelize.INTEGER,
            primaryKey: true
        },
        image: {
            type: Sequelize.STRING
        },
        longitude: {
            type: Sequelize.STRING
        },
        lattitude: {
            type: Sequelize.STRING
        },
        taken: {
            type: Sequelize.BOOLEAN
        }
    });
  
    parkingspot.associate = function(models){
        parkingspot.hasOne(models.account,{foreignKey:'parkingLotId'})
  }
  
  return parkingspot;
  }