/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pig.impl.logicalLayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.pig.data.DataType;
import org.apache.pig.impl.logicalLayer.parser.ParseException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.plan.OperatorKey;
import org.apache.pig.impl.plan.PlanVisitor;
import org.apache.pig.impl.plan.VisitorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LOMapLookup extends ExpressionOperator {
    private static final long serialVersionUID = 2L;

    /**
     * The key to lookup along with the type and schema corresponding to the
     * type and schema of the value linked to the key
     */
    private ExpressionOperator mMap;
    private Object mMapKey;
    private byte mValueType;
    private Schema mValueSchema;
    private static Log log = LogFactory.getLog(LOMapLookup.class);

    /**
     * 
     * @param plan
     *            Logical plan this operator is a part of.
     * @param k
     *            Operator key to assign to this node.
     * @param map
     *            the map expression
     * @param mapKey
     *            key to look up in the map. The key is of atomic type
     * @param valueType
     *            type of the value corresponding to the key
     * @param valueSchema
     *            schema of the value if the type is tuple
     */
    public LOMapLookup(LogicalPlan plan, OperatorKey key, ExpressionOperator map,
            Object mapKey, byte valueType, Schema valueSchema)
            throws ParseException {
        super(plan, key);

        if (!DataType.isAtomic(DataType.findType(mapKey))) {
            throw new ParseException("Map key " + mapKey.toString()
                    + " is not atomic");
        }
        mMap = map;
        mMapKey = mapKey;
        mValueType = valueType;
        mValueSchema = valueSchema;
    }

    public ExpressionOperator getMap() {
        return mMap;
    }

    public Object getLookUpKey() {
        return mMapKey;
    }

    public byte getValueType() {
        return mValueType;
    }

    @Override
    public String name() {
        return "MapLookup " + mKey.scope + "-" + mKey.id;
    }

    @Override
    public boolean supportsMultipleInputs() {
        return false;
    }

    @Override
    public Schema getSchema() {
        return mSchema;
    }

    @Override
    public Schema.FieldSchema getFieldSchema() {
        if (!mIsFieldSchemaComputed && (null == mFieldSchema)) {
            Schema.FieldSchema fss;
            if (DataType.isSchemaType(mValueType)) {
                fss = new Schema.FieldSchema(null, mValueSchema);
            } else {
                fss = new Schema.FieldSchema(null, DataType
                        .findType(mValueType));
            }

            mIsFieldSchemaComputed = true;
        }
        return mFieldSchema;
    }

    @Override
    public void visit(LOVisitor v) throws VisitorException {
        v.visit(this);
    }

}
