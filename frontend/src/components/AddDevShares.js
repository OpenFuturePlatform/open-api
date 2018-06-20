import React from 'react';
import {connect} from 'react-redux';
import {withVisible} from '../components-ui/withVisible';
import {Button, Input, Modal} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {addShareHolder} from '../actions/dev-shares';

class AddDevSharesComponent extends React.Component {

  state = {
    address: '',
    share: '',
    addressError: '',
    shareError: '',
    isAddressErrorVisible: false,
    isShareErrorVisible: false,
    isSaving: false,
  };

  onAddressChange = e => this.setState({address: e.target.value});
  onShareChange = e => this.setState({share: Math.abs(e.target.value)});

  onSubmit = async e => {
    const {scaffold} = this.props;
    const {address, share} = this.state;
    this.setState({isSaving: true});
    try {
      await this.props.actions.addShareHolder(scaffold, {address, share});
      this.props.onHide();
    } catch (e) {
      console.log(e)
    }
    this.setState({isSaving: false});
  };

  render() {
    const {isVisible, onShow, onHide} = this.props;
    const {isSaving, share, address} = this.state;

    return (
      <span>
      <Button fluid attached='top' onClick={onShow}>Add Share</Button>
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>New Share Holder</Modal.Header>
            <Modal.Content>
              <div>Address:</div>
              <Input fluid value={address} disabled={isSaving} onChange={this.onAddressChange}/>
              <div>Share:</div>
              <Input type="number" min={0} step={1} fluid value={share} disabled={isSaving} onChange={this.onShareChange}/>
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={isSaving} icon='checkmark' labelPosition='right' content='Save' onClick={this.onSubmit}/>
            </Modal.Actions>
        </Modal>
    </span>
    );
  }
}

const mapStateToProps = (state, {scaffold}) => ({scaffold});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({addShareHolder}, dispatch)
});

export const AddDevShares = connect(mapStateToProps, mapDispatchToProps)(withVisible(AddDevSharesComponent));
