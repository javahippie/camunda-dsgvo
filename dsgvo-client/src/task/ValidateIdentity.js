import React, {Component} from 'react'
import {Button, ButtonToolbar, Notification} from "rsuite"
import './Task.css'
import {withRouter} from "react-router-dom"
import {fetchTaskData, completeTask} from "./TaskClient"
import {getValueNullSafe} from "../util/CamundaUtil"

class ValidateIdentity extends Component {

    constructor(props) {
        super(props)
        this.state = {
            task: {},
            taskId: this.props.match.params.id,
            identityIsValid: false
        }

        this.handleAccept = this.handleAccept.bind(this)
        this.handleReject = this.handleReject.bind(this)
    }

    componentDidMount() {
        fetchTaskData(this.state.taskId,
            (result => this.setState(result)),
            (error => {
                Notification['error']({
                    title: 'Fehlgeschlagen',
                    description: 'Aktion nicht möglich'
                })
            }))
    }

    handleAccept() {
        this.handleAnswer(true)
    }

    handleReject() {
        this.handleAnswer(false)
    }

    handleAnswer(identityIsValid) {
        completeTask(this.state.taskId,
            {
                variables: {
                    identityConfirmed: {
                        value: identityIsValid,
                        type: 'Boolean'
                    }
                }
            }, (response => {
                Notification['success']({
                    title: 'Erfolg',
                    description: 'Aktion erfolgreich durchgeführt'
                })
                this.props.history.push('/tasks')
            }),
            (error => {

            }))
    }

    render() {
        return <div className="task-form">
            <h1>Identität des Anfragenden bestätigen</h1>
            <h2>Personendaten</h2>
            <ul>
                <li>{getValueNullSafe(this.state.firstName)} {getValueNullSafe(this.state.lastName)}</li>
                <li>{getValueNullSafe(this.state.street)}</li>
                <li>{getValueNullSafe(this.state.zipCode)} {getValueNullSafe(this.state.city)}</li>
            </ul>
            <h2>Kontaktdaten</h2>
            <ul>
                <li>{getValueNullSafe(this.state.eMail)}</li>
                <li>{getValueNullSafe(this.state.phoneNumber)}</li>
            </ul>
            <h2>Anfrage</h2>
            <span>{getValueNullSafe(this.state.requestText)}</span>

            <ButtonToolbar>
                <Button onClick={this.handleAccept} appearance="primary">Identität bestätigt</Button>
                <Button onClick={this.handleReject} appearance="default">Identität nicht bestätigt</Button>
            </ButtonToolbar>
        </div>

    }
}

export default withRouter(ValidateIdentity)