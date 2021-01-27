import React, {Component} from 'react'
import './CancelProcess.css'
import PersonalDataForm from "../newrequest/PersonalDataForm";
import ContactDataForm from "../newrequest/ContactDataForm";
import {Redirect} from "react-router-dom";
import {Notification} from "rsuite";

class CancelProcess extends Component {

    constructor(props) {
        super(props)
        this.state = {instanceId: this.props.match.params.id,
                     currentForm: 0}
    }

    renderCurrentForm() {
        switch (this.state.currentForm) {
            case 0:
                return <PersonalDataForm completePage={(partForm) => this.completePage(partForm)}/>
            case 1:
                return <ContactDataForm completePage={(partForm) => {
                    this.completePage(partForm, this.cancelProcess)
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

    cancelProcess() {
        const formValue = this.state.formValue

        console.log('Creating Process')
        console.log(formValue)

        fetch('/engine-rest/message', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                {
                    messageName: 'cancel-request',
                    processInstanceId: this.state.instanceId,
                    processVariables: {
                        firstNameCancel: {
                            value: formValue.firstName,
                            type: 'String'
                        },
                        lastNameCancel: {
                            value: formValue.lastName,
                            type: 'String'
                        },
                        streetCancel: {
                            value: formValue.street,
                            type: 'String'
                        },
                        zipCodeCancel: {
                            value: formValue.zipCode,
                            type: 'String'
                        },
                        cityCancel: {
                            value: formValue.city,
                            type: 'String'
                        },
                        phoneNumberCancel: {
                            value: formValue.phoneNumber,
                            type: 'String'
                        },
                        eMailCancel: {
                            value: formValue.eMail,
                            type: 'String'
                        },
                    },
                })
        }).then(response => {
            if (response.ok) {
                this.successMessage()
            } else {
                this.errorMessage()
            }
        })
    }

    successMessage() {
        Notification['success']({
            title: 'Erfolgreich',
            description: `Stornierungsanfrage eingetragen`
        })
    }

    errorMessage() {
        Notification['error']({
            title: 'Fehlgeschlagen',
            description: `Stornierung konnte nicht zugeordnet werden.`
        })
    }

    render() {
        return <div className="cancel-form">
            <h1>Stornierung erfassen</h1>
            {this.renderCurrentForm()}
        </div>
    }
}

export default CancelProcess