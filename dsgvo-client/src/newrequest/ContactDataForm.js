import React, {Component} from 'react'
import {
    Button,
    Form,
    FormGroup,
    ControlLabel,
    FormControl,
    ButtonToolbar,
    Schema, Message
} from 'rsuite'

import './NewRequest.css'

const {StringType} = Schema.Types;

const model = Schema.Model({
    phoneNumber: StringType().pattern(/^\+49[0-9]*/,'Keine gültige, deutsche Telefonnummer').isRequired('Bitte eine Telefonnummer angeben'),
    eMail: StringType()
        .isEmail('Keine gültige Email')
        .isRequired('Bitte eine Email angeben'),
});

class ContactDataForm extends Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)

        this.state = {
            formValue: {
                phoneNumber: '',
                eMail: '',
            }
        }
    }

    handleSubmit() {
        if (!this.form.check()) {
            console.error('Form Error');
            return;
        }
        this.props.completePage(this.state.formValue)
    }

    render() {
        return <div className="new-task">
            <Message description="Bitte geben Sie die Kontaktdaten des Anfragenden an"/>
            <Form fluid
                  ref={ref => (this.form = ref)}
                  onChange={formValue => {
                      this.setState({formValue})
                  }}
                  onCheck={formError => {
                      this.setState({formError});
                  }}
                  model={model}>
                <h2>Kontaktdaten</h2>
                <FormGroup>
                    <ControlLabel>E-Mail</ControlLabel>
                    <FormControl name="eMail" type="email"/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Telefon</ControlLabel>
                    <FormControl name="phoneNumber" type="phone"/>
                </FormGroup>
                <FormGroup>
                    <ButtonToolbar>
                        <Button onClick={this.handleSubmit} appearance="primary">Weiter</Button>
                    </ButtonToolbar>
                </FormGroup>
            </Form>
        </div>
    }

}

export default ContactDataForm