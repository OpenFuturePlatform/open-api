import React, {Component} from "react";
import {Button, Divider, Dropdown, Input, Modal} from "semantic-ui-react";

import {withVisible} from "../components-ui/withVisible";
import {withSaving} from "../components-ui/withSaving";
import styled from "styled-components";
import {t} from "../utils/messageTexts";
import {Datepicker} from "../components-ui/Datepicker";


const KeyGenerateContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

class GatewayApplicationKeyGenerateComponent extends Component {
  state = {
    expiredDate: null,
    gatewayId : null
  };

  onShow = () => {
    const { onShow, gateway } = this.props;

    this.setState({gatewayId: gateway.id });

    onShow();
  };

  onBackgroundClick = e => {
    const { onHide, isSaving } = this.props;
    const target = e.target;
    if (isSaving || !target.classList.contains('modals')) {
      return;
    }
    onHide();
  };

  onSubmit = async () => {
    const { submitWithSaving } = this.props;
    const { expiredDate } = this.state;
    this.setState({ isSaving: true });

    submitWithSaving({ expiredDate });
  };

  render() {
    const { isVisible, onHide, isSaving } = this.props;

    return (
      <KeyGenerateContainer>
        <Button primary type="button" floated="right" onClick={this.onShow}>
          Generate New Keys
        </Button>
        <Modal size="tiny" open={isVisible} onClose={this.onBackgroundClick}>
          <Modal.Header>Generate Application Key</Modal.Header>
          <Modal.Content>
            <Datepicker date={this.state.expiredDate} onChange={expiredDate => this.setState({ expiredDate })} />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              icon="checkmark"
              labelPosition="right"
              content="Generate"
              onClick={this.onSubmit}
            />
          </Modal.Actions>
        </Modal>
      </KeyGenerateContainer>
    );
  }
}

export const GatewayKeyGenerate = withVisible(withSaving(GatewayApplicationKeyGenerateComponent));
