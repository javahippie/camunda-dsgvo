import React, {Component} from 'react'
import {Steps, Notification,} from 'rsuite'

import './NewRequest.css'
import {withRouter, Redirect} from "react-router-dom"
import moment from 'moment'
import PersonalDataForm from "./PersonalDataForm"
import ContactDataForm from "./ContactDataForm"
import RequestDataForm from "./RequestDataForm"

class NewRequest extends Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)
        this.handleCancel = this.handleCancel.bind(this)

        this.state = {
            formValue: {
                firstName: '',
                lastName: '',
                street: '',
                zipCode: '',
                city: '',
                phoneNumber: '',
                eMail: '',
                startDate: '',
                requestText: ''
            },
            currentForm: 0
        }
    }

    handleSubmit() {
        if (!this.form.check()) {
            console.error('Form Error')
            return
        }
        this.createProcess(this.state.formValue)
    }

    handleCancel() {
        this.props.history.goBack()
    }


    buildBusinessKey({lastName, firstName, startDate}) {
        return `${firstName} ${lastName} ${moment(startDate).format('YYYY-MM-DD')}`
    }

    successMessage(taskId) {
        Notification['success']({
            title: 'Erfolgreich',
            description: `Prozess mit Schlüssel ${taskId} gestartet`
        })
    }

    errorMessage() {
        Notification['error']({
            title: 'Fehlgeschlagen',
            description: `Prozess konnte nicht gestartet werden.`
        })
    }

    renderCurrentForm() {
        switch (this.state.currentForm) {
            case 0:
                return <PersonalDataForm completePage={(partForm) => this.completePage(partForm)}/>
            case 1:
                return <ContactDataForm completePage={(partForm) => this.completePage(partForm)}/>
            case 2:
                return <RequestDataForm completePage={(partForm) => {
                    console.log('Completing the creation')
                    console.log(partForm)
                    this.completePage(partForm, this.createProcess)
                }}/>
            default:
                return <Redirect to="/tasks"/>

        }
    }

    completePage(partData, callback) {
        this.setState({
            formValue: {...this.state.formValue, ...partData},
            currentForm: this.state.currentForm + 1
        }, callback)
    }

    createProcess() {
        const formValue = this.state.formValue

        console.log('Creating Process')
        console.log(formValue)

        fetch('/engine-rest/process-definition/key/DSGVO_Process/start', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                {
                    variables: {
                        firstName: {
                            value: formValue.firstName,
                            type: 'String'
                        },
                        lastName: {
                            value: formValue.lastName,
                            type: 'String'
                        },
                        street: {
                            value: formValue.street,
                            type: 'String'
                        },
                        zipCode: {
                            value: formValue.zipCode,
                            type: 'String'
                        },
                        city: {
                            value: formValue.city,
                            type: 'String'
                        },
                        phoneNumber: {
                            value: formValue.phoneNumber,
                            type: 'String'
                        },
                        eMail: {
                            value: formValue.eMail,
                            type: 'String'
                        },
                        startDate: {
                            value: moment(formValue.startDate).format('YYYY-MM-DDTHH:mm:ss.SSSZZ'),
                            type: 'Date'
                        },
                        requestText: {
                            value: formValue.requestText,
                            type: 'String'
                        }
                    },
                    businessKey: this.buildBusinessKey(this.state.formValue)
                })
        }).then(response => {
            if (response.ok) {
                response.json().then(content => this.successMessage(content.businessKey))
            } else {
                this.errorMessage('error')
            }
        })
    }

    render() {
        return <div className="new-task">
            <h1>Neue Anfrage erfassen</h1>
            <Steps current={this.state.currentForm}>
                <Steps.Item title="Personendaten erfassen"/>
                <Steps.Item title="Kontaktdaten erfassen"/>
                <Steps.Item title="Anfrage erfassen"/>
            </Steps>

            {this.renderCurrentForm()}

        </div>
    }

}

export default withRouter(NewRequest)