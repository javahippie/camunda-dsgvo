import React, {Component} from 'react'
import {
    Button,
    Form,
    FormGroup,
    ControlLabel,
    FormControl,
    ButtonToolbar,
    Message,
    Schema
} from 'rsuite'

const {StringType} = Schema.Types;

const model = Schema.Model({
    firstName: StringType().isRequired('Bitte einen Vornamen angeben'),
    lastName: StringType().isRequired('Bitte einen Nachnamen angeben'),
    street: StringType().isRequired('Bitte eine Straße angeben'),
    zipCode: StringType().pattern(/^[0-9]{5}$/, 'Keine gültige PLZ').isRequired('Bitte eine PLZ angeben'),
    city: StringType().isRequired('Bitte eine Stadt angeben')
});

class PersonalDataForm extends Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)

        this.state = {
            formValue: {
                firstName: '',
                lastName: '',
                street: '',
                zipCode: '',
                city: '',
                phoneNumber: '',
                eMail: '',
            },
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
            <Message description="Bitte geben Sie die Personendaten des Anfragenden an"/>
            <Form fluid
                  ref={ref => (this.form = ref)}
                  onChange={formValue => {
                      this.setState({formValue})
                  }}
                  onCheck={formError => {
                      this.setState({formError});
                  }}
                  model={model}>
                <h2>Personendaten</h2>
                <FormGroup>
                    <ControlLabel>Nachname</ControlLabel>
                    <FormControl name="lastName"/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Name</ControlLabel>
                    <FormControl name="firstName"/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Straße</ControlLabel>
                    <FormControl name="street"/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Postleitzahl</ControlLabel>
                    <FormControl name="zipCode"/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Ort</ControlLabel>
                    <FormControl name="city"/>
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

export default PersonalDataForm