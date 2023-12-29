# App protocol for communication between nodes and gateway

## 1. Connect

- Client connect to server via IP addr and port
- Client: `Ping`
- Server: `200 Pong`

## 2. Register a node to gateway

- Client: `register <NODE_TYPE> <GROUP_NAME>`

  `NODE_TYPE`:
    - 1: Temperature sensor
    - 2: Blood pressure sensor
    - 3: ECG (heart beat)
    
  `GROUP_NAME`:
  
    Each monitor device has multiple sensors aka. our `node`. Many `nodes` can have a same GROUP_NAME to group all the data into one presentation.
  
- Server:
  - If the group with `GROUP_NAME` does not exist:
  
    `300 Group and node registered`

  - If the group with `GROUP_NAME` exists, but there's no registered node which is `NODE_TYPE`:
  
    `301 Node registered to a group`
  
  - If the group with `GROUP_NAME` exists and there is already a node which is `NODE_TYPE`:
  
    `309 Group already has similar data source`
    
**Note**: Client has 2 configurations must be hold on server, don't reimplement with new type of client ID, try reusing connection ID of the worker thread which is responsible for that client.

## 2. Data transmitting

- Client: 
