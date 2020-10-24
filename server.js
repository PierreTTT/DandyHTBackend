var Sequelize = require('sequelize-cockroachdb');
var fs = require('fs');

// Connect to CockroachDB through Sequelize.
var sequelize = new Sequelize('test', 'testuser', 'testpassword', {
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

// Define the Account model for the "accounts" table.
var Account = sequelize.define('accounts', {
    id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    balance: {
        type: Sequelize.INTEGER
    }
});

// Create the "accounts" table.
Account.sync({
        force: true
    })
    .then(function () {
        // Insert two rows into the "accounts" table.
        return Account.bulkCreate([{
                id: 1,
                balance: 1000
            },
            {
                id: 2,
                balance: 250
            }
        ]);
    })
    .then(function () {
        // Retrieve accounts.
        return Account.findAll();
    })
    .then(function (accounts) {
        // Print out the balances.
        accounts.forEach(function (account) {
            console.log(account.id + ' ' + account.balance);
        });
        process.exit(0);
    })
    .catch(function (err) {
        console.error('error: ' + err.message);
        process.exit(1);
    });