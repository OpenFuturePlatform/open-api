import React, {Component} from 'react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import '../css/table.css';
import {EtherscanLink} from '../components-ui/EtherscanLink';
import {Icon, Segment} from 'semantic-ui-react';
import {addShareHolder, fetchDevShares, removeShareHolder} from '../actions/dev-shares';
import {AddDevShares} from './AddDevShares';
import {RemoveDevShare} from './RemoveDevShare';

const getColumns = (scaffold) => [
  {
    Header: 'Share Holder Address',
    accessor: 'address',
    Cell: ({value}) => <EtherscanLink>{value}</EtherscanLink>,
    sortable: false
  },
  {
    Header: 'Holder Share',
    accessor: 'share',
    width: 200,
    Cell: ({value}) => <span>{value}%</span>,
    sortable: false
  },
  {
    Header: '',
    accessor: 'address',
    width: 150,
    Cell: ({value}) => (
      <span>
        <Icon link name='edit' size='large'/>{' '}
        <RemoveDevShare scaffold={scaffold} holderAddress={value} />
      </span>
    ),
    sortable: false,
  }
];

export class DevSharesComponent extends Component {

  componentDidMount() {
    const {scaffold} = this.props;
    this.props.actions.fetchDevShares(scaffold);
  }

  handleOnRemoveShareHolder = (holderAddress) => {
    const {scaffold} = this.props;
    return this.props.actions.removeShareHolder(scaffold, holderAddress);
  };

  render() {
    const {devShares, scaffold} = this.props;

    return (
      <div className="table-with-add">
        <AddDevShares scaffold={scaffold}/>
        <Segment attached styles={{padding: 0}}>
          <ReactTable data={devShares} columns={getColumns(scaffold)}
                      className="-striped" showPagination={false} resizable={false} minRows={1}
                      pageSize={devShares.length}
          />
        </Segment>
      </div>
    );
  }
}

const mapStateToProps = ({devShares}, {scaffold}) => ({scaffold, devShares});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({
    fetchDevShares,
    addShareHolder,
    removeShareHolder
  }, dispatch)
});

export const DevShares = connect(mapStateToProps, mapDispatchToProps)(DevSharesComponent);
