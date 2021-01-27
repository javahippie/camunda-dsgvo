import React, {Component} from 'react'
import {Button, Checkbox, Notification} from "rsuite"
import './Task.css'
import {withRouter} from "react-router-dom"
import {fetchTaskData, completeTask} from "./TaskClient"
import {getValueNullSafe} from "../util/CamundaUtil"


class CreateFormalRequest extends Component {

    constructor(props) {
        super(props)
        this.state = {
            task: {},
            taskId: this.props.match.params.id,
            isAccounting: false,
            isHR: false,
            isSales: false
        }
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    componentDidMount() {
        fetchTaskData(this.state.taskId,
            (result => this.setState(result)),
            (error => {
            }))
    }

    handleSubmit() {
        completeTask(this.state.taskId,
            {
                variables: {
                    searchInAccounting: {
                        value: this.state.isAccounting,
                        type: 'Boolean'
                    },
                    searchInSales: {
                        value: this.state.isSales,
                        type: 'Boolean'
                    }, searchInHr: {
                        value: this.state.isHR,
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

    render() {
        return <div className="task-form">
            <h1>Datenquellen auswählen</h1>
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

            <Checkbox onChange={(value, checked) => this.setState({isAccounting: checked})}>Buchhaltung</Checkbox>
            <Checkbox onChange={(value, checked) => this.setState({isHR: checked})}>Personalwesen</Checkbox>
            <Checkbox onChange={(value, checked) => this.setState({isSales: checked})}>Vertrieb</Checkbox>
            <Button appearance="primary" onClick={this.handleSubmit}>Abschließen</Button>
        </div>

    }
}

export default withRouter(CreateFormalRequest)