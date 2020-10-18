import * as React from 'react';
import {FunctionComponent} from 'react';
import ReactDOM = require('react-dom');

const Regions: FunctionComponent = () => (
    <p>Hello from React</p>
)

ReactDOM.render(
    <Regions/>,
    document.getElementById('root')
)
