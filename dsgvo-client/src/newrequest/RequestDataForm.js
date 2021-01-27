import React, {Component} from 'react'
import {
    Button,
    DatePicker,
    Form,
    FormGroup,
    ControlLabel,
    FormControl,
    ButtonToolbar,
    Schema, Message
} from 'rsuite'

const {StringType, DateType} = Schema.Types;

const model = Schema.Model({
    startDate: DateType().isRequired('Bitte ein Startdatum angeben'),
    requestText: StringType().isRequired('Bitte einen Anfragetext eingeben')
});

class RequestDataForm extends Component {

    constructor(props) {
        super(props)
        this.handleSubmit = this.handleSubmit.bind(this)

        this.state = {
            formValue: {
                startDate: null,
                requestText: ''
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
            <Message description="Bitte geben Sie das Eingangsdatum und den Text der Anfrage an" />
            <Form fluid
                  ref={ref => (this.form = ref)}
                  onChange={formValue => {
                      this.setState({formValue})
                  }}
                  onCheck={formError => {
                      this.setState({formError});
                  }}
                  model={model}>
                <h2>Anfrage</h2>
                <FormGroup>
                    <ControlLabel>Anfragedatum</ControlLabel>
                    <FormControl name="startDate" accepter={DatePicker}/>
                </FormGroup>
                <FormGroup>
                    <ControlLabel>Anfragetext</ControlLabel>
                    <FormControl rows={10} cols={10} name="requestText" componentClass="textarea"/>
                </FormGroup>
                <FormGroup>
                    <ButtonToolbar>
                        <Button onClick={this.handleSubmit} appearance="primary">Submit</Button>
                    </ButtonToolbar>
                </FormGroup>
            </Form>
        </div>
    }

}

export default RequestDataForm