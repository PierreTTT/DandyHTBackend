const dotenv = require("dotenv");
dotenv.config();

const { sequelize, account, parkingspot } = require("./models/index");
const locations = require("./mockLocations/locations");
const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const { createServer } = require("http");

const port = process.env.PORT || 8080;

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());

app.use(
  cors({
    origin: `*`,
    credentials: true,
  })
);

// REST API calls
// User will park

// Is the spot occupied?
app.post("/grey", async (req, res) => {
  const { taken, idUser, idParking } = req.body;

// will change the state of the parking spot
  const parkingSpot = await parkingspot
    .findOne({ where: { id: idParking } })
    .then(function (data) {
      if (data) {
        const returnValue = data
          .update({
            taken,
          })
          .then(function (data) {
            return data;
          });
        return returnValue;
      }
    });

    // user will gain points
  let returnPoints;
  await account.findOne({ where: { id: idUser } }).then(function (data) {
    if (data) {
      const currentPoints = data.points;
      const pointValue = taken? 4:10;
      const addPoints = currentPoints + pointValue;
      returnPoints = addPoints;
      data.update({
        points: addPoints,
      });
    }
  });
  return res.send({
    points: returnPoints,
    parkingState: parkingSpot.get({ plain: true }),
  });
});

app.post("/login", async (req, res) => {
  const value = await account
    .findOne({
      where: {
        username: req.body.username,
        password: req.body.password,
      },
    })
    .then((data) => {
      const dt = data.get({ plain: true });
      return {
        username: dt.username,
        points: dt.points,
        parkingLotId: dt.parkingLotId,
      };
    });
  return res.send(value);
});

app.post("/points", async (req, res) => {
  const {  idUser } = req.body;
    const value = await account
      .findOne({
        where: {
          id:idUser
        },
      })
      .then((data) => {
        const dt = data.get({ plain: true });
        return {
          points: dt.points,
        };
      });
    return res.send(value);
  });

(async () => {
  await sequelize
    .sync({
      force: true,
    })
    .then(function () {
      parkingspot.bulkCreate(locations);
      account.bulkCreate([
        {
          id: 1,
          username: "admin",
          password: "admin",
          points: 0,
        },
      ]);
    })
    .catch(function (err) {
      console.error("error: " + err.message);
      process.exit(1);
    });

  createServer(app).listen(port, () => {
    console.info(`Server running on port ${port}`);
  });
})();
