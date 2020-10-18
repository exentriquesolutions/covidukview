import * as React from 'react';
import {FunctionComponent} from 'react';
import ReactDOM = require('react-dom');
import {Col, Container, Row} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Regions from "./Regions";


const App: FunctionComponent = () => (
    <Container>
        <Row>
            <Col>
                <h1>Weekly rolling average of cases per region</h1>
                <Regions />
            </Col>
        </Row>
    </Container>
)

ReactDOM.render(
    <App/>,
    document.getElementById('root')
)
