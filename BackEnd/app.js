// app.js
import express from 'express';

const app = express();

// Basic route
app.get('/', (req, res) => {
  res.send('Hello, World!');
});

export default app;
