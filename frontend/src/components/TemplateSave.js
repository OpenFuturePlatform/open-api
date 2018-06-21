import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Button, Input, Message, Modal} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {saveTemplate} from '../actions/contract-templates';

class TemplateSaveView extends Component {

  state = {
    isModalVisible: false,
    name: '',
    nameError: ''
  };

  setInputFocus = element => {
    if (element) {
      element.focus();
    }
  };

  onNameChange = (e) => {
    e.preventDefault();
    this.setState({name: e.target.value});
  };

  onNameSubmit = (e) => {
    if (e.key !== 'Enter' || !this.state.name) {
      return;
    }

    return this.handleOnSaveTemplate(e);
  };

  handleOnShowModal = async (e) => {
    e.preventDefault();
    this.setState({isModalVisible: true, name: '', nameError: ''});
  };

  handleOnHideModal = async (e) => {
    e.preventDefault();
    this.setState({isModalVisible: false, name: '', nameError: ''});
  };

  handleOnSaveTemplate = async (e) => {
    const {actions, fields} = this.props;
    const name = this.state.name;
    e.preventDefault();

    try {
      await actions.saveTemplate({...fields, name});
      this.setState({isModalVisible: false});
    } catch (e) {
      const status = e.response ? e.response.status : null;
      const message = e.response ? e.response.message : '';
      const nameError = status === 409 ? 'Name already exists.': message;
      this.setState({nameError});
    }
  };

  renderErrors = () => {
    if (!this.state.nameError) {
      return null;
    }

    return (
      <Message style={{
        paddingLeft: '14px',
        paddingTop: '7px',
        paddingBottom: '7px',
        marginTop: '4px',
        marginBottom: '10px'
      }} error list={[this.state.nameError]}/>
    );
  };

  render() {
    return (
      <span>
        <Button secondary onClick={this.handleOnShowModal}
                style={{
                  marginBottom: '10px',
                  marginRight: '15px',
                }}>
          Save Template
        </Button>
        <Modal size="mini" open={this.state.isModalVisible} onClose={this.handleOnHideModal}>
          <Modal.Header>Save Scaffold Template</Modal.Header>
          <Modal.Content>
            <Input ref={this.setInputFocus}
                   value={this.state.name}
                   onChange={this.onNameChange}
                   onKeyPress={this.onNameSubmit}
                   fluid
                   placeholder='Template Name' />
            {this.renderErrors()}
          </Modal.Content>
          <Modal.Actions>
            <Button negative onClick={this.handleOnHideModal}>Cancel</Button>
            <Button positive
                    disabled={!this.state.name}
                    onClick={this.handleOnSaveTemplate}
                    icon='checkmark'
                    labelPosition='right'
                    content='Save'/>
          </Modal.Actions>
        </Modal>
      </span>
    );
  }
}

const mapStateToProps = (state, {fields}) => ({fields});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({saveTemplate}, dispatch),
});

export const TemplateSave = connect(mapStateToProps, mapDispatchToProps)(TemplateSaveView);
