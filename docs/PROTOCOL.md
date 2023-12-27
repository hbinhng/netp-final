# App protocol for communication between nodes and gateway

## 1. Connect

- Client connect to server via IP addr and port
- Client: ping
- Server: 200 pong

## 2. Register a node

- Client: register &lt;clientID&gt;
- Server: Check if this ID exist and online state
  - if nodeID is existed and online then response: `400 Node Already Existed`
  - if nodeID is existed and offline then `turn the node status on server to ONLINE` and response: `200 Node Comes Back Online`
  - if nodeID not existed then `Create a node and set its status online` and response: `200 Node Registered`

## 2. Communication

- Client: &lt;heartbeat data&gt; and &lt;temperature data&gt;
- Server: gateways receive data and save it to the database then return `201 OK`
