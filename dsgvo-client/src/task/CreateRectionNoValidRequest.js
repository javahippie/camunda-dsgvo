import React, {Component} from 'react'
import {Button, ButtonToolbar, Notification, Message} from "rsuite";
import './Task.css'
import {withRouter} from "react-router-dom";
import {fetchTaskData, completeTask} from "./TaskClient"
import {getValueNullSafe} from "../util/CamundaUtil"

class CreateRectionNoValidRequest extends Component {

    constructor(props) {
        super(props)
        this.state = {
            task: {},
            taskId: this.props.match.params.id,
            hrData: null,
            accountingData: null,
            salesData: null
        }
        this.handleAccept = this.handleAccept.bind(this)
    }

    componentDidMount() {
        fetchTaskData(this.state.taskId,
            (result => this.setState(result)),
            (error => {
            }))
    }

    handleAccept() {
        completeTask(this.state.taskId,
            {}, (response => {
                console.log(response)
                Notification['success']({
                    title: 'Erfolg',
                    description: 'Aktion erfolgreich durchgeführt'
                })
                this.props.history.push('/tasks')
            }),
            (error => {
                Notification['error']({
                    title: 'Fehlgeschlagen',
                    description: 'Aktion nicht möglich'
                })
            }))
    }

    render() {
        return <div className="task-form">
            <h1>Anfrage validieren</h1>
            <Message
                type="info"
                description="Bitte verfassen Sie eine manuelle Absage. Es handelt sich nicht um eine gültige Anfrage nach DSGVO."
            />
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
                <Button onClick={this.handleAccept} appearance="primary">Absage gesendet</Button>
            </ButtonToolbar>
        </div>

    }
}

export default withRouter(CreateRectionNoValidRequest)