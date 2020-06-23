import React from "react";

export default class TopBar extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let items = this.props.items;
        return (
            <div>
                {(items.length === 0) && <div align="center"><h3> {this.props.message} </h3></div>}
                { <div>
                    <hr style={items.length === 0 ? { visibility:"hidden"} : {visibility:"unset"}}/>
                    <input type="button" value="Prev" onClick={this.props.prevHandler} style={this.props.prevCounter === 0 ? {visibility:"hidden"} : {visibility:"unset"}}/>
                    <input type="button" value="Next" onClick={this.props.nextHandler} style={!this.props.showNext ? {visibility:"hidden"} : {visibility:"unset"}}/>
                    <div style={{display:"flex", justifyContent:"center"}}>
                        <label>
                            <h3 style={{color:"#0ccac4"}}>Sort by: </h3>
                            <select onChange={(event)=>this.props.sortHandler(event.target.value)} style={{width: "10em"}}>
                                {this.props.sortOptions.map((option,index) => {
                                  return <option value={option} key={index} > {option} </option>
                                })}
                            </select>
                        </label>
                        <label>
                            <h3 style={{color:"#0ccac4"}}>Filter by:</h3>
                            <select onChange={(event)=>this.props.filterHandler(event.target.value)} style={{width: "10em"}}>
                                {this.props.filterOptions.map((option,index) => {
                                    return <option value={option} key={index}> {option} </option>
                                })}
                            </select>
                        </label>
                        <label>
                            <h3 style={{color:"#0ccac4"}}>Only women shoes: </h3>
                            <input type="checkbox" checked={this.props.onlyWomenShoes} onChange={event => this.props.womenShoesHandler(event.target.checked)}/>
                        </label>
                    </div>
                </div> }
            </div>
        );
    }
}