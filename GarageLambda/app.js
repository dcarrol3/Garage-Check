var AWS = require('aws-sdk');
var ApiBuilder = require('claudia-api-builder'),
  api = new ApiBuilder();

module.exports = api;

api.get('/status', function (req) {
    return process.env.doorStatus;
});

api.post('/setstatus', function (req) {
    var state = req.post.status;
    process.env.doorStatus = state;
    return "Door is set to " + state;
});

