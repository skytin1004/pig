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

import org.apache.pig.impl.plan.OperatorKey;
import org.apache.pig.impl.plan.PlanVisitor;
import org.apache.pig.impl.plan.VisitorException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class LONegative extends UnaryExpressionOperator {

    private static final long serialVersionUID = 2L;

    /**
     * 
     * @param plan
     *            Logical plan this operator is a part of.
     * @param k
     *            Operator key to assign to this node.
     * @param operand
     *            the only operand for a unary operator
     */
    public LONegative(LogicalPlan plan, OperatorKey k,
            ExpressionOperator operand) {
        super(plan, k, operand);
    }

    @Override
    public Schema getSchema() {
        return mSchema;
    }

    @Override
    public Schema.FieldSchema getFieldSchema() {
        return mFieldSchema;
    }

    @Override
    public void visit(LOVisitor v) throws VisitorException {
        v.visit(this);
    }

    @Override
    public String name() {
        return "Negative " + mKey.scope + "-" + mKey.id;
    }

    // Return type is the same as input
    // This might not be a number type which is wrong
    // but the post-parsing logic will eventually detect it.
    @Override
    public byte getType() {
        return mOperand.getType() ;
    }
    
}
