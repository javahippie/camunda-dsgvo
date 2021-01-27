import {Component} from "react"
import {BrowserRouter as Router, Route, Link} from "react-router-dom"
import {Nav, Navbar, Icon} from "rsuite"
import TaskList from "../tasklist/TaskList"
import React from "react"
import NewRequest from "../newrequest/NewRequest"
import Home from "../home/Home"
import ValidateRequest from "../task/ValidateRequest";
import CreateFormalRequest from "../task/CreateFormalRequest";
import ValidateIdentity from "../task/ValidateIdentity";
import ManualResultCheck from "../task/ManualResultCheck";
import CreateFormalRejection from "../task/CreateFormalRejection";
import CreateRectionNoValidRequest from "../task/CreateRectionNoValidRequest";
import RunningRequests from "../running/RunningRequests";
import CancelProcess from "../cancel/CancelProcess";

class NavBar extends Component {

    constructor(props) {
        super(props)
        this.handleSelect = this.handleSelect.bind(this)
        this.state = {
            activeKey: null
        }
    }

    handleSelect(eventKey) {
        this.setState({
            activeKey: eventKey
        })
    }

    render() {
        const NavLink = props => <Nav.Item componentClass={Link} {...props} />;
        return <Router>
            <div>
                <Navbar appearance="inverse">
                    <Navbar.Body>
                        <Nav onSelect={this.handleSelect} activeKey={this.state.activeKey}>
                            <NavLink eventKey="1" to="/">Home</NavLink>
                            <NavLink eventKey="2" to="/requests">Laufende Anfragen</NavLink>
                            <NavLink eventKey="3" to="/tasks">Meine Aufgaben</NavLink>
                        </Nav>
                        <Nav pullRight>
                            <NavLink eventKey="4"
                                     to="/new"
                                     icon={<Icon icon="plus" />}>Neue Anfrage</NavLink>
                        </Nav>
                    </Navbar.Body>
                </Navbar>

                <Route exact path="/" component={Home}/>
                <Route path="/new" component={NewRequest}/>
                <Route path="/tasks" component={TaskList}/>
                <Route path="/requests" component={RunningRequests}/>
                <Route path="/task/Task_ValidateRequest/:id" component={ValidateRequest} />
                <Route path="/task/Task_CreateFormalRequest/:id" component={CreateFormalRequest} />
                <Route path="/task/Task_ValidateIdentity/:id" component={ValidateIdentity} />
                <Route path="/task/Task_ManualResultCheck/:id" component={ManualResultCheck} />
                <Route path="/task/Task_CreateFormalRejection/:id" component={CreateFormalRejection} />
                <Route path="/task/Task_CommunicateRejectionIdentityNotValid/:id" component={CreateRectionNoValidRequest} />
                <Route path="/cancel/:id" component={CancelProcess} />

            </div>
        </Router>
    }
}


export default NavBar
