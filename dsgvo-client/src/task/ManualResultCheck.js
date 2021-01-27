import React, {Component} from 'react'
import {Button, ButtonToolbar, Notification, Timeline} from "rsuite";
import './Task.css'
import {withRouter} from "react-router-dom";
import {fetchTaskData, completeTask} from "./TaskClient"
import {getValueNullSafe} from "../util/CamundaUtil"

class ManualResultCheck extends Component {

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
        this.handleReject = this.handleReject.bind(this)
    }

    componentDidMount() {
        fetchTaskData(this.state.taskId,
            (result => this.setState(result)),
            (error => {
            }))
    }

    handleAccept() {
        this.handleAnswer(true)
    }

    handleReject() {
        this.handleAnswer(false)
    }

    handleAnswer(reportApproved) {
        completeTask(this.state.taskId,
            {
                variables: {
                    approvalGiven: {
                        value: reportApproved,
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
                Notification['error']({
                    title: 'Fehlgeschlagen',
                    description: 'Aktion nicht möglich'
                })
            }))
    }

    arrayStringToArray(field) {
        if (field && field.value) {
            return JSON.parse(field.value)
        } else {
            return []
        }
    }

    render() {
        return <div className="task-form">
            <h1>Anfrage validieren</h1>
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

            <h2>Ermittelte Daten</h2>
            <h3>Buchhaltung</h3>
            <Timeline>
                {this.arrayStringToArray(this.state.accountingData).map((value) =>
                    <Timeline.Item>{value}</Timeline.Item>)}
            </Timeline>
            <h3>Personalwesen</h3>
            <Timeline>
                {this.arrayStringToArray(this.state.hrData).map((value) => <Timeline.Item>{value}</Timeline.Item>)}
            </Timeline>
            <h3>Vertrieb</h3>
            <Timeline>
                {this.arrayStringToArray(this.state.salesData).map((value) => <Timeline.Item>{value}</Timeline.Item>)}
            </Timeline>

            <ButtonToolbar>
                <Button onClick={this.handleAccept} appearance="primary">Bericht freigeben</Button>
                <Button onClick={this.handleReject} appearance="default">Bericht ablehnen</Button>
            </ButtonToolbar>
        </div>

    }
}

export default withRouter(ManualResultCheck)