import React from 'react';
import {connect} from 'react-redux';
import {withVisible} from '../components-ui/withVisible';
import {Button, Icon, Modal} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {removeShareHolder} from '../actions/dev-shares';

class RemoveDevShareComponent extends React.Component {

  state = {
    isSaving: false,
  };

  onSubmit = async e => {
    const {scaffold, holderAddress} = this.props;
    this.setState({isSaving: true});
    try {
      await this.props.actions.removeShareHolder(scaffold, holderAddress);
      this.props.onHide();
    } catch (e) {
      console.log(e);
    }
    this.setState({isSaving: false});
  };

  render() {
    const {isVisible, onShow, onHide, holderAddress} = this.props;
    const {isSaving} = this.state;

    return (
      <span>
        <Icon link name='remove' size='large' onClick={onShow}/>
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Remove Share Holder</Modal.Header>
            <Modal.Content>
              You are removing Share Holder {holderAddress}. Are you sure?
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={isSaving} icon='checkmark' labelPosition='right'
                      content='Remove' onClick={this.onSubmit}/>
            </Modal.Actions>
          </Modal>
      </span>
    );
  }
}

const mapStateToProps = (state, {scaffold, holderAddress}) => ({scaffold, holderAddress});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({removeShareHolder}, dispatch)
});

export const RemoveDevShare = connect(mapStateToProps, mapDispatchToProps)(withVisible(RemoveDevShareComponent));
